#!/bin/bash

VERSION_LABEL=$(grep version tailwind/target/maven-archiver/pom.properties | cut -d= -f2)

BUNDLE_NAME=tailwind-$VERSION_LABEL.zip
BUNDLE_LOCATION=tailwind-distribution/target/$BUNDLE_NAME

curl -X POST --user "${BB_AUTH_STRING}" "https://api.bitbucket.org/2.0/repositories/${BITBUCKET_REPO_OWNER}/${BITBUCKET_REPO_SLUG}/downloads" --form files=@"${BUNDLE_LOCATION}"
