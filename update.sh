#!/bin/bash
mvn exec:java
mv *.json ../worldretroday.github.io/_data/events
cd ../worldretroday.github.io/_data/events
git add .
git commit -m "updated events"
git push
cd -

