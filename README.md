Note: the project:kvstore_example is just a demo, no performance, no crash recovery, no reliability.

## env:

Linux/java8



## build:

<!--after clone project-->

mvn clean install
## test example
cd kvstore_admin/target/kvstore_admin/bin

<!-- the param:1 is kv number(million) -->

sh startrace test example 1

## test race
cd kvstore_admin/target/kvstore_admin/bin

<!-- the param:1 is kv number(million) -->

sh startrace test race 1