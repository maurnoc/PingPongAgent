JC = javac


MKDIR = mkdir -p
CLSDIR = bin/
CLS = $(addprefix ${CLSDIR},MainAgent.class AgentController.class Agent.class PingAgent.class PongAgent.class)

.PHONY: all mainagent clean

all: ${CLSDIR} mainagent $(CLS) | ${CLSDIR}

${CLSDIR}MainAgent.class: ${CLSDIR} MainAgent.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} MainAgent.java

${CLSDIR}AgentController.class: ${CLSDIR} AgentController.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} AgentController.java

${CLSDIR}Agent.class: ${CLSDIR} Agent.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} Agent.java

${CLSDIR}PingAgent.class: ${CLSDIR} PingAgent.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} PingAgent.java

${CLSDIR}PongAgent.class: ${CLSDIR} PongAgent.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} PongAgent.java

mainagent: ${CLSDIR} | ${CLSDIR}
	$(JC) -d ${CLSDIR} MainAgent.java AgentController.java Agent.java	PingAgent.java PongAgent.java

${CLSDIR}:
	$(MKDIR) ${CLSDIR}
	$(MKDIR) ${CLSDIR}

clean:
	$(RM)r ${CLSDIR}
