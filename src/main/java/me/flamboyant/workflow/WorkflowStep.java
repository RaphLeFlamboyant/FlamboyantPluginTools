package me.flamboyant.workflow;

import java.util.HashMap;

public class WorkflowStep<EEventType extends Enum<EEventType>, EStepType extends Enum<EStepType>> {
    private HashMap<EEventType, EStepType> eventToStep;
    private EStepType stepType;

    public WorkflowStep(EStepType stepType, HashMap<EEventType, EStepType> eventToStep) {
        this.stepType = stepType;
        this.eventToStep = eventToStep;
    }

    public EStepType getStepType() {
        return stepType;
    }

    public boolean isValidEvent(EEventType eventType) {
        return eventToStep.containsKey(eventType);
    }

    public EStepType getTriggeredStep(EEventType eventType) {
        return eventToStep.get(eventType);
    }
}
