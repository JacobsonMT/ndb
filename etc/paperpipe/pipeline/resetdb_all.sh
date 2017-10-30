#!/bin/bash
set -eu

if [[ $# -lt 1 ]]; then
    echo "Usage:"
    echo "$0 PAPERID (Optional, CASE)"
    echo "CASE can be all, annovar, variant, raw_variant, raw_key_value, or paper"
    exit -1
fi


case="all"
if [ -z ${2+x} ]; then echo "Deleting all tables from paper# $1"; else case=$2 ; fi

DELETE_ANNOVAR=" DELETE FROM variant where paper_id=$1; "
DELETE_VARIANT=" DELETE FROM variant where paper_id=$1; "
DELETE_RAWVARIANT=" DELETE FROM raw_variant where paper_id=$1; "
DELETE_RAWKV=" DELETE FROM raw_key_value where paper_id=$1; "
DELETE_PAPER=" DELETE FROM papers where id=$1; "

source db_credentials.sh
DATABASE_IN=" mysql -u$DBUSER -p$DBPASS "
DATABASE_NAME=" marvdb_staging "

PAPERMARKER="[$1,"
export PAPERMARKER

if [[ "$case"=="all" ]]; then
    echo "use $DATABASE_NAME; $DELETE_ANNOVAR $DELETE_VARIANT $DELETE_RAWVARIANT $DELETE_RAWKV $DELETE_PAPER " | $DATABASE_IN
    find commits/ -name "*_paper$1.out" -exec rm {} \; 

    # Delete paper
    paperCommit=$(grep -F $PAPERMARKER commits/paper* /dev/null | cut -f1 -d":")
    echo "Removing $paperCommit"
    rm $paperCommit
    echo "Commit files for paper $1 deleted"

fi

if [[ "$case"=="annovar" ]]; then
    #echo "use $DATABASE_NAME; $DELETE_ANNOVAR " | $DATABASE_IN 
    rm commits/$case"_paper"$1.out
    echo "'$case' commit file deleted."       
fi

if [[ "$case"=="variant" ]]; then    
    echo "use $DATABASE_NAME;  $DELETE_VARIANT " | $DATABASE_IN 
    echo ";variant; deleted for paper $1"
    rm commits/$case"_paper"$1.out
    echo "'$case' commit file deleted."   
fi

if [[ "$case"=="raw_variant" ]]; then
    echo "use $DATABASE_NAME;  $DELETE_RAWVARIANT " | $DATABASE_IN 
    echo ";raw_variant; deleted for paper $1"
    rm commits/$case"_paper"$1.out
    echo "'$case' commit file deleted."   
fi

if [[ "$case"=="raw_key_value" ]]; then
    echo "use $DATABASE_NAME; $DELETE_RAWKV  " | $DATABASE_IN 
    echo ";raw_key_value; deleted for paper $1"
    rm commits/$case"_paper"$1.out
    echo "'$case' commit file deleted."   
fi

if [[ "$case"=="paper" ]]; then
    echo "use $DATABASE_NAME; $DELETE_PAPER " | $DATABASE_IN
    echo ";papers; deleted for paper $1"
    
    # Delete paper
    paperCommit=$(grep -F $PAPERMARKER commits/paper* /dev/null | cut -f1 -d":")
    rm $paperCommit
    echo "'$case' commit file deleted."   
fi
