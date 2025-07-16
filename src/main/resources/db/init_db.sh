#!/bin/bash
echo "Current directory: $(pwd)"
echo "Listing /dbscripts:"
ls -la /dbscripts

echo "Waiting for Oracle DB to be ready..."
sleep 60

echo "Running init.sql..."
sqlplus system/MyStrongPassword123@oracle-xe-composed:1521/XEPDB1 @/dbscripts/init.sql

#echo "Running add_package.sql..."
#sqlplus system/MyStrongPassword123@oracle-xe-composed:1521/XEPDB1 @/dbscripts/add_package.sql

echo "Database initialization complete."
