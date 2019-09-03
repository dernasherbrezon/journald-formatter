## About [![Build Status](https://travis-ci.org/dernasherbrezon/journald-formatter.svg?branch=master)](https://travis-ci.org/dernasherbrezon/journald-formatter) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ru.r2cloud%3Ajournald-formatter&metric=alert_status)](https://sonarcloud.io/dashboard?id=ru.r2cloud%3Ajournald-formatter)

This project provides special JUL formatter. It will format log levels as numbers instead of strings. These numbers are from syslog format. Using these numbers could help you assign proper log level in journald. See example below

## Example configuration

Here is example configuration:

```
java.util.logging.ConsoleHandler.formatter=ru.r2cloud.logging.JournaldFormatter
java.util.logging.JournaldFormatter.format=<%4$s>%5$s%6$s [%2$s]%n
```

It looks like normal SimpleFormatter configuration. The trick is in ```<%4$s>```. When java process output log entries with such prefix, journald can parse them and assign proper level. Later on, these log entries could be searched, parsed, analyzed via journalctl:

```
journalctl -u myservice -p err -n 20 
```

