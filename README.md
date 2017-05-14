# dynamic-testdata
Resolves dynamic test data from a text and json. Useful stuff for "data driven testing".

Base class to load test data entry from json:
 - JsonTestDataLoader
 
The list of default resolver values:
 - ${uuid} - returns random UUID
 - ${uuid:[name]} - returns random uuid and assigns the value of uuid to the specified alias.
 - ${date} - returns current date time in 'Date' format of RDF
 - ${date:[-][01:02:03.004]} - current date time - 1 hour 2 minutes 3 seconds 4 milliseconds
 - ${date:[+][01:02:03.004]} - current date time + 1 hour 2 minutes 3 seconds 4 milliseconds
 - ${datetime} - returns current date time in 'DateTime' format of RDF
 - ${datetime:[+][01:02:03.004]} - current date time - 1 hour 2 minutes 3 seconds 4 milliseconds
 - ${#parameter} - returns the value of input test parameter, for example: ${#environemnt}
 - ${{@literal @}name} - returns the value of formatted string for specified name
 - ${{@literal @}name:[arg1][argN]} - returns the value of formatted string for specified name and arguments
