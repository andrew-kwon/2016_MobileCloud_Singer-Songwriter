<?php

		$meetName = $_GET['MeetName'];
		$ornerID = $_GET['OrnerID'];
		
		$setDate = $_GET['setDate'];
		$setTime = $_GET['setTime'];
		$setPlaceName=$_GET['setPlaceName'];
		$setLatitude =$_GET['setLatitude'];
		$setLongtitude = $_GET['setLongtitude'];
		$setContent = $_GET['setContent'];
		$ornerName = $_GET['OrnerName'];

#		$profilePic = $_GET['profile'];		
		if($meetName == '' ){
			echo 'please fill meetName';
		}else{
			

	$con = mysqli_connect(HOST,USER,PASS,DB);		#require_once('dbConnect.php');
			$sql = "SELECT * FROM meetUpList WHERE meetName='$meetName' AND ornerID='$ornerID'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo '이미 같은 이름의 모임을 만드셨습니다.';
			}else{				
$sql = "INSERT INTO meetUpList(meetName,meetPlaceName,placeLati,placeLongti,meetDate,meetTime, content, ornerName, ornerID) VALUES('$meetName','$setPlaceName','$setLatitude','$setLongtitude','$setDate','$setTime','$setContent','$ornerName','$ornerID')";

	if(mysqli_query($con,$sql)){
$sql2 = "INSERT into partyList (MeetName,OrnerID,myUserID,authority) VALUES('$meetName','$ornerID','$ornerID',1)";
	
	mysqli_query($con,$sql2);	

	echo 'successfully Upload';
	}else{
		echo 'oops! Please try again!';
	}      


		mysqli_close($con);
}
}
?>