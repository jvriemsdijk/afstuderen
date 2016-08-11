# The script MUST contain a function named azureml_main
# which is the entry point for this module.
#
# The entry point function can contain up to two input arguments:
#   Param<application>: a pandas.DataFrame containing the applications
#   Param<adjustments>: a pandas.DataFrame containing the adjustments
def azureml_main(applicationDF=None, adjustments=None):

    # Copy the application dataframe
    application = applicationDF.copy(True)

    # Calculate the maximum number of adjustments per application
    nMaxAdjustments = adjustments['application'].value_counts().max()

    for n in range(0, nMaxAdjustments):

        # Group adjustments by their application id
        groupedDF = adjustments.groupby('application')

        # Select the first instance of adjustment for each application
        selectedRows = groupedDF.first()

        # Clean the selected rows of id's
        selectedRowsClean = selectedRows.get(['actual_cost', 'cost_subsidized', 'name'])

        # Join the cleaned rows with the application dataframe
        application = application.join(selectedRowsClean, on='application_id', rsuffix='_{0}'.format(n), lsuffix='')

        # In the first run, calculate the totals of the actual cost and cost subsidized
        if n == 0:
            application = application.set_index('application_id')
            print 'Total number of sums calculated: {0}'.format(groupedDF['actual_cost'].sum().size)
            application['actual_cost_total'] = groupedDF['actual_cost'].sum().copy()
            application['cost_subsidized_total'] = groupedDF['cost_subsidized'].sum().copy()
            application = application.reset_index()

        # Drop the already joined rows from the adjustment dataframe
        adjustments = adjustments.set_index('adjustment_id')
        adjustments = adjustments.drop(selectedRows['adjustment_id'])
        adjustments = adjustments.reset_index()

        # Remove NaN values from the dataframe
    for i in application:
        if application[i].dtype == 'object':
            application[i] = application[i].fillna('')
        elif application[i].dtype == 'float64':
            application[i] = application[i].fillna(0)

    return [application]