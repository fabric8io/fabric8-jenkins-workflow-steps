# Fabric8 DevOps Workflow Steps

Provides Jenkins Workflow Steps for working with Fabric8 DevOps

## Steps available

The following jenkins workflow steps are available if you add this plugin to your Jenkins 

### hubot

Allows sending of a message to the hubot chat bot

```
hubot room: 'foo', message: 'hello world!'
```

If the room `foo` does not already exist its created on the fly

### hubotProject

Allows sending of a message to the default chat room for a project build (using the `fabric8.yml` file to find the chat room for a project).

If no chat room is defined for a project then it uses the environment variable `$FABRIC8_DEFAULT_HUBOT_ROOM`

```
hubotProject 'hello world!'
```

