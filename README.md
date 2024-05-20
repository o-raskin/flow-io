# [Fly.io - Distributed systems challenge](https://fly.io/dist-sys/)

Application that used as node for simulation processing data in distributed system.
Testing based on [Maelstrom](https://github.com/jepsen-io/maelstrom) framework.

## [Challenge #1 - Echo](https://fly.io/dist-sys/1/)
Your job is to send a message with the same body back to the client but with a message type of "echo_ok". It should also associate itself with the original message by setting the "in_reply_to" field to the original message ID. This reply field is handled automatically if you use the Node.Reply() method.

## [Challenge 2 - Unique ID Generation](https://fly.io/dist-sys/2/)
In this challenge, you’ll need to implement a globally-unique ID generation system that runs against Maelstrom’s unique-ids workload. Your service should be totally available, meaning that it can continue to operate even in the face of network partitions.

#### ...

## Run

1. Build package with Maven:
> mvn package

2. Run with maelstrom: \

Challenge 1:
> ./maelstrom/maelstrom test -w echo --bin jrun.sh --time-limit 5

Challenge 2:
> ./maelstrom/maelstrom test -w unique-ids --bin jrun.sh --time-limit 30 --rate 1000 --node-count 3 --availability total --nemesis partition
