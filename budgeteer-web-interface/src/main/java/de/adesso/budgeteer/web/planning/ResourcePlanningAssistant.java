package de.adesso.budgeteer.web.planning;

import java.util.ArrayList;
import java.util.List;

public class ResourcePlanningAssistant {

    private Configuration configuration;

    private List<Allocation> allocations = new ArrayList<>();

    /**
     * Initializes a new ResourcePlanningAssistant.
     *
     * @param configuration the configuration parameters used for calculations
     */
    public ResourcePlanningAssistant(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Allocate a person to a task and recalculate all values.
     */
    public void allocate(Task task, Person person, Percent workload){
        Allocation allocation = new Allocation(task, person, workload);
        task.getAllocations().add(allocation);
        person.getAllocations().add(allocation);
        allocations.add(allocation);
        task.recalculate(configuration);
        person.recalculate(configuration);
    }

    /**
     * Deallocate a person from a task and recalculate all values.
     */
    public void deallocate(Task task, Person person){
        Allocation allocationToRemove = null;
        for(Allocation allocation : allocations){
            if(allocation.getPerson() == person && allocation.getTask() == task){
                allocationToRemove = allocation;
                break;
            }
        }
        this.allocations.remove(allocationToRemove);
        task.getAllocations().remove(allocationToRemove);
        person.getAllocations().remove(allocationToRemove);

        task.recalculate(configuration);
        person.recalculate(configuration);
    }


}
