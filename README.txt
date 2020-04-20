This is a revised Chrome favorites webscrapper using junit testing, hiberante, and maven. The code designed to be configurable via the parameters table and each class exlcuding main to be as independent of each as possible. Some of the configuration allows for changing user_agent, scrap timeout, email configuration, and json file source while the program is still operational.

[Use]
Designed to run autonomously to grab a json file, parse its chosen url values, grab data from urls via multithreading and configurable data paths, parse the aforementioned data as datetimes, update database with relevant data, and send out an email with any new information that has updated today.

