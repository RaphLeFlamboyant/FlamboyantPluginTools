package me.flamboyant.workflow;

public interface WorkflowVisitor<EStepType extends Enum<EStepType>, TIdentifier> {
    void onWorkflowStart(TIdentifier identifier);
    void onNextStep(EStepType stepType, TIdentifier identifier);
    void onWorkflowEnd(TIdentifier identifier);
}
