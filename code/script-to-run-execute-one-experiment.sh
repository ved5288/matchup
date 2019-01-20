#!bin/bash

array=( 1000 2000 3000 10000)

for students in "${array[@]}"
do
	courses=$(($students/5))
	lq=3
	hq=12
	for i in {1..10};
	do
		echo java main.Main "<../experiments/"$students-$courses-$lq-$hq/inst$i/cli-iterativeHR
		java main.Main <../experiments/$students-$courses-$lq-$hq/inst$i/cli-iterativeHR >../experiments/$students-$courses-$lq-$hq/inst$i/stdout-iterativeHR
		echo java main.Main "<../experiments/"$students-$courses-$lq-$hq/inst$i/cli-firstPreference
		java main.Main <../experiments/$students-$courses-$lq-$hq/inst$i/cli-firstPreference >../experiments/$students-$courses-$lq-$hq/inst$i/stdout-firstPreference	
	done
done
