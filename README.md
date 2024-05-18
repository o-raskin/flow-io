Maven:
> mvn package

Maelstrom run example:
> ./maelstrom/maelstrom test -w echo --bin jrun.sh --time-limit 5

> ./maelstrom/maelstrom test -w unique-ids --bin jrun.sh --time-limit 30 --rate 1000 --node-count 3 --availability total --nemesis partition