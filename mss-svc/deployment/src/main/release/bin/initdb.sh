#!/usr/bin/env bash
###############################################################################
# Copyright 2016 Huawei Technologies Co., Ltd.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
###############################################################################

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

LIB_PATH=$SCRIPT_DIR/../webapps/ROOT/WEB-INF/lib

BUCKETS_ROOT="$(mktemp -d)"
trap "rm -rf $BUCKETS_ROOT" EXIT

cd $BUCKETS_ROOT
unzip $LIB_PATH/org.openo.sdno.mss.init*.jar buckets/*

for D in dummy/bucketsys buckets/*; do
    dbname=$(basename $D)
    mysql -uroot -proot -e"drop database if exists $dbname;"
    mysql -uroot -proot -e"create database $dbname;"
done

java -cp "$LIB_PATH/*" org.openo.sdno.mss.init.StartInit

echo "====Init finished======"
