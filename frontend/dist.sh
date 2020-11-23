#!/bin/sh
app_name=$(cat package.json | jq '.name' -r)
app_version=$(cat package.json | jq '.version' -r)
archive="${app_name}-${app_version}.tar.gz"
ng.sh build --environment 'production-nec-test'
tar -czf "$archive" "dist" --transform='s/\dist/\/static/'
cp "$archive" "/var/www/repository/adsb-frontend"
