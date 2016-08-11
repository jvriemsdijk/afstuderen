# The script MUST contain a function named azureml_main
# which is the entry point for this module.

# imports up here can be used to
import pandas as pd


# The entry point function can contain up to two input arguments:
#   Param<dataframe1>: a pandas.DataFrame
#   Param<dataframe2>: a pandas.DataFrame
def azureml_main(dataframe1=None, dataframe2=None):

    # Select the model name of each of the given dataframes
    modelOne = dataframe1["Model"][0]
    modelTwo = dataframe2["Model"][0]

    # Determine the mean best precision and recall for each option
    precisionList = []
    recallList = []
    for i in dataframe1.axes[1]:
        if i.find("Precision") >= 0:

            if dataframe1[i][10] > dataframe2[i][10]:
                precisionList.append(modelOne)
            elif dataframe1[i][10] < dataframe2[i][10]:
                precisionList.append(modelTwo)
            else:
                precisionList.append("No difference")
        if i.find("Recall") >= 0:
            if dataframe1[i][10] > dataframe2[i][10]:
                recallList.append(modelOne)
            elif dataframe1[i][10] < dataframe2[i][10]:
                recallList.append(modelTwo)
            else:
                recallList.append("No difference")

    # Get the most precise model
    mostPreciseModel = "No difference"
    if precisionList.count(modelOne) > precisionList.count(modelTwo):
        mostPreciseModel = modelOne
    elif precisionList.count(modelOne) < precisionList.count(modelTwo):
        mostPreciseModel = modelTwo

    # Get the model with the best recallbestRecallModel = "No difference"
    bestRecallModel = "No difference"
    if recallList.count(modelOne) > recallList.count(modelTwo):
        bestRecallModel = modelOne
    elif recallList.count(modelOne) < recallList.count(modelTwo):
        bestRecallModel = modelTwo

    # Determine the model dataframe to return
    modelToReturn = "No difference"
    if mostPreciseModel != "No difference" and bestRecallModel != "No difference":
        if mostPreciseModel == bestRecallModel:
            modelToReturn = mostPreciseModel
    else:
        if mostPreciseModel != "No difference":
            modelToReturn = mostPreciseModel
        elif bestRecallModel != "No difference":
            modelToReturn = bestRecallModel

    # In case of no difference return a random model
    if modelToReturn == "No difference":
        import random
        if random.random() >= 0.5:
            return dataframe1
        else:
            return dataframe2

    # Else return the dataframe of the best found model
    elif modelToReturn == modelOne:
        return dataframe1
    else:
        return dataframe2
