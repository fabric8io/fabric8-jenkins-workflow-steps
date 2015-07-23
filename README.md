# Fabric8 DevOps Workflow Steps

Provides Jenkins Workflow Steps for working with [Fabric8 DevOps](http://fabric8.io/guide/cdelivery.html) in particular for clean integration with the [Hubot chat bot](https://hubot.github.com/) and human approval

<p align="center">
  <a href="http://fabric8.io/guide/cdelivery.html">
  	<img src="https://raw.githubusercontent.com/fabric8io/fabric8/master/docs/images/cover/cover_small.png" alt="fabric8 logo"/>
  </a>
</p>


The following jenkins workflow steps are available if you add this plugin to your Jenkins:

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

### hubotApprove

Sends a hubot message the project chat room for a project when the build is waiting for user input with the hubot commands to proceed or abort the build.

```
hubotApprove "Do you want to stage?"
input id: 'Proceed', message: "Staging?"
```

Here's an example of it in action inside [Let's Chat](http://sdelements.github.io/lets-chat/):

![example of the approval in action with LetsChat](images/approve.png "hubotApprove inside Let's Chat")

