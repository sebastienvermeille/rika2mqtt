# Module plugins-api

Expose API that can be extended by 3rd parties plugins to let developers connect and bring extra behaviours to rika2mqtt.

## Architecture of this module

### Responsibilities

Loads external plugins at startup
Ensure provides some entry points extendable by plugins.

### Opinionated choices

**Question:** Why PF4J ?

**Answer:** I did some projects with it and it is actuvely maintained. I really like PF4J is very easy to use,
no XML configuration and let the job done!

### How does it work ?

https://pf4j.org/

