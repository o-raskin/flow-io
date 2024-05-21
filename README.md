# [Fly.io - Distributed systems challenge](https://fly.io/dist-sys/)

An application that serves as one of the nodes in a cluster for simulating processing and managing data within a distributed system.\
Testing based on [Maelstrom](https://github.com/jepsen-io/maelstrom) framework.

## [Challenge #1 - Echo](https://fly.io/dist-sys/1/)
Your job is to send a message with the same body back to the client but with a message type of "echo_ok". 

## [Challenge 2 - Unique ID Generation](https://fly.io/dist-sys/2/)
In this challenge, you’ll need to implement a globally-unique ID generation system that runs against Maelstrom’s unique-ids workload. 
Your service should be totally available, meaning that it can continue to operate even in the face of network partitions.

#### ...

## Request example

**init**:
```json
{"src": "c1", "dest": "n1", "id": 1, "body": {"type": "init", "msg_id": 1, "node_id": "n1", "node_ids": ["n1","n2"]}}
```

**echo**:
```json
{"src": "c1", "dest": "n1", "id": 2, "body": {"type": "echo", "msg_id": 2, "echo": "USER_ECHO_MSG"}}
```

**generate**:
```json
{"src": "c1", "dest": "n1", "id": 3, "body": {"type": "generate", "msg_id": 3}}
```

## Run

1. Build package with Maven:
> mvn package

2. Run with maelstrom: 

Challenge 1:
> ./maelstrom/maelstrom test -w echo --bin jrun.sh --time-limit 5

Challenge 2:
> ./maelstrom/maelstrom test -w unique-ids --bin jrun.sh --time-limit 30 --rate 1000 --node-count 3 --availability total --nemesis partition
