package nl.h2.ejb;

import nl.h2.ejb.schema.ConditionJPA;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrainingDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(TrainingDataGenerator.class.getName());

    public TrainingDataGenerator() {
    }
    @PersistenceContext(unitName = "tdg.database")
    private EntityManager entityManager;

    Map<ConditionJPA, Double> conditionChanceMap;




    public void generateTrainingData(int numberOfRecords, double averageNumberOfApplications, double sigmaNumberOfApplications) {

    }



    private void clearTrainingData() {
        // TODO : method to delete all training data
    }

    private void initTrainingData() {

    }




}
