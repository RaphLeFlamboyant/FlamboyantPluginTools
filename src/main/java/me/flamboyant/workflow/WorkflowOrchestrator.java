package me.flamboyant.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowOrchestrator<EEventType extends Enum<EEventType>, EStepType extends Enum<EStepType>, TIdentifier> {
    private HashMap<TIdentifier, EStepType> identifierToStep = new HashMap<>();
    private Map<EStepType, WorkflowStep<EEventType, EStepType>> stepTypeToWorkflowStep;
    private WorkflowStep<EEventType, EStepType> startingStep;
    private List<WorkflowVisitor<EStepType, TIdentifier>> visitors = new ArrayList<>();

    public WorkflowOrchestrator(List<WorkflowStep<EEventType, EStepType>> workflowSteps) {
        stepTypeToWorkflowStep = workflowSteps.stream().collect(Collectors.toMap(i -> i.getStepType(), item -> item));
        startingStep = workflowSteps.get(0);
    }

    public void addVisitor(WorkflowVisitor<EStepType, TIdentifier> visitor) {
        visitors.add(visitor);
    }

    public void removeVisitor(WorkflowVisitor<EStepType, TIdentifier> visitor) {
        visitors.remove(visitor);
    }

    public void startWorkflow(TIdentifier identifier) {
        if (!identifierToStep.containsKey(identifier)) {
            identifierToStep.put(identifier, startingStep.getStepType());

            for (WorkflowVisitor<EStepType, TIdentifier> visitor : visitors) {
                visitor.onWorkflowStart(identifier);
            }
        }
    }

    public void onEventTriggered(EEventType eventType, TIdentifier identifier) {
        if (!identifierToStep.containsKey(identifier)) return;

        EStepType stepType = identifierToStep.get(identifier);
        if (!stepTypeToWorkflowStep.containsKey(stepType)) return;

        WorkflowStep<EEventType, EStepType> currentStep = stepTypeToWorkflowStep.get(stepType);
        if (!currentStep.isValidEvent(eventType)) return;

        EStepType nextStepType = currentStep.getTriggeredStep(eventType);

        if (stepTypeToWorkflowStep.containsKey(nextStepType)) {
            for (WorkflowVisitor<EStepType, TIdentifier> visitor : visitors) {
                visitor.onNextStep(nextStepType, identifier);
            }
            identifierToStep.put(identifier, nextStepType);
        }
        else {
            for (WorkflowVisitor<EStepType, TIdentifier> visitor : visitors) {
                visitor.onWorkflowEnd(identifier);
            }
            identifierToStep.remove(nextStepType);
        }
    }
}
