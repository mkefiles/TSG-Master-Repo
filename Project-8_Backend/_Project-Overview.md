# Project Overview

## Helpful Notes

- Start RabbitMQ container: `docker run -d --hostname my-rabbit -p 5672:5672 --name some-rabbit rabbitmq:3`
  - Downloads the necessary *Image* (if unavailable) w/ a *Name* of `rabbitmq` and a *Tag* of `3`
    - It is important to specify the `--hostname` that way it is not randomized and you can keep track of the data
  - Starts a *Container* with a *Name* of `some-rabbit` and an *Image* reference of `rabbitmq:3`
  - Default Port: 5672
  - **Note:** This initializes a *Volume* by default
  - **Note:** Without explicitly mapping the ports (in the `run` command), RabbitMQ is only accessible from within Docker (i.e., not on your local computer)
- Check Rabbit Logs: `docker logs some-rabbit`
  - Information retrieved from logs:
    - Volume Directory (`home dir`): */var/lib/rabbitmq*
    - Configuration File:
      - */etc/rabbitmq/conf.d/10-defaults.conf*, **and**...
      - */etc/rabbitmq/conf.d/20-management_agent.disable_metrics_collector.conf*
      - Database Directory (incl. Node Name of `my-rabbit`): */var/lib/rabbitmq/mnesia/rabbit@my-rabbit*
- Access RabbitMQ Shell: `docker exec -it <container-name-or-id> /bin/bash`
  - Get status: `rabbitmq-diagnostics status`

**Notes:**
- You can gain access to a Management Console if you use the `management` version of the *rabbitmq* Docker Image
  - Ex: Version 3 of *rabbitmq* -- `rabbitmq:3` vs. `rabbitmq:3-management`
  - Access Management Console at *localhost:8080* and use `guest` / `guest` for username and password respectively
    - It is possible to change the default username and password with this Docker command [see Docker Hub](https://hub.docker.com/_/rabbitmq)

## Challenge
Create a simple REST API that, when given a JSON payload from the *Client*, will pass a *Message* to the Operations Team telling them that an "HRA Configuration" was created. The *Message* would typically be sent via email, however this example will simply log a message to the console.

### Requirements
These are the requirements set forth by the Challenge:

1. A `POST` end-point to the *API* module: */api/employers/{employerId}/hra-config*
   1. Receive the request (see sample below)
   2. Log the *configuration* (simulate the "persistence" with the console)
   3. Immediately return an HTTP 201 Created status code
   4. Publish the message to RabbitMQ
2. A RabbitMQ listener (in the *Consumer* module)
   1. Listens to `employer.hra-config.queue`
   2. Logs a mock “Ops notification” to console

**Note:**
- API Module Name: `api` || `appointment-api` (Package Literal || Modulith Override)
- Publisher Module Name: `publisher` || `notification-service` (Package Literal || Modulith Override)
- Consumer Module Name: `consumer` || `notification-listener` (Package Literal || Modulith Override)
  - In my layout of this project, I decided to create a three-app Modular Monolith as it seemed to be a bit cleaner and allows me to further experiment with Spring Modulith

**Sample Input**

```JSON
{
    "planYear": "2026",
    "monthlyAllowance": 350.00
}
```

**Sample Output**

```JSON
{
    "eventId": "uuid",
    "occurredAt": "ISO-8601",
    "employerId": "string",
    "planYear": "2026",
    "monthlyAllowance": 350.00
}
```

### RabbitMQ Setup

| Component | Name | Purpose |
|------------|-------|----------|
| Exchange | `gravie.employers.exchange` | Topic exchange |
| Routing Key | `employer.hra-config.created` | Message route |
| Queue | `employer.hra-config.queue` | Consumer endpoint |

### Acceptance Criteria

- API returns `201` and echoes `employerId` + `planYear`.
- Publisher sends JSON to `gravie.employers.exchange` using routing key `employer.hra-config.created`.
- **Unit Tests**
  - Controller: validates body, returns 400 on invalid input.
  - Publisher: verify `convertAndSend()` called once with correct args.
  - Listener: given a mock message, logs expected output.
- Message includes required fields; invalid payloads are rejected before publish.

## Unit Test

### Successful Run
The following is the output message from Postman:

```
ConfigurationDTO{planYear='2026', monthlyAllowance=350.0}
```

The following is the successful output message to the console:

```
OPS NOTIFICATION: ConfigurationMessageDTO{eventId=20db5170-d8b7-415b-a77d-1664ae20e36b, occurredAt=2025-11-07T22:44:31.191199Z, employerId='301', planYear='2026', monthlyAllowance=350.0}
```