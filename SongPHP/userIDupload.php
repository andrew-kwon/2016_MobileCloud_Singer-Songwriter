<?php

		$userID =$_GET['userID'];
		$userImageName = $_GET['userImageName'];		
		if($userImageName == '' ){
			echo 'please fill all values';
		}else{
			

		$con = mysqli_connect(HOST,USER,PASS,DB) or die("접속불가");

			#require_once('dbConnect.php');
			$sql = "SELECT * FROM ImageName WHERE userID='$userID'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo 'NOPE';
			}else{				
$sql = "INSERT INTO ImageName(userID,userImageName) VALUES('$userID','$userImageName')";
	if(mysqli_query($con,$sql)){
		echo 'successfully Upload';
	}else{
		echo 'oops! Please try again!';
	}      


		mysqli_close($con);
}
}
?>
