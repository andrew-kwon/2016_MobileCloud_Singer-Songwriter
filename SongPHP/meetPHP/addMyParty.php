<?php
		$myUserID = $_GET["myUserID"];
		$MeetName = $_GET["MeetName"];
		$OrnerID = $_GET["OrnerID"];

		if($myUserID==''){
			echo 'please fill all values';
		}else{
			
		$con = mysqli_connect(HOST,USER,PASS,DB);			$sql = "SELECT * FROM partyList WHERE MeetName="$MeetName" and OrnerID="$OrnerID" and myUserID="$myUserID"";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo 'already partied';
			}
			else{

		$sql = "INSERT into partyList (MeetName,OrnerID,myUserID) VALUES("$MeetName","$OrnerID","$myUserID")";
		if(mysqli_query($con,$sql)){
			echo 'Successed join!';

		$sql2 ="UPDATE meetUpList set countPeople = (countPeople+1) where meetName = "$MeetName" and ornerID="$OrnerID"";
		if(mysqli_query($con,$sql2)){ echo ' sucess update';}
		}
		}
	mysqli_close($con);

	}	


