<?php

		$username = $_GET['username'];
		$userID = $_GET['userID'];
		
		$songName = $_GET['songName'];
		$contents = $_GET['contents'];
		$filepath=$_GET['filepath'];
#		$profilePic = $_GET['profile'];		
		if($username == '' ){
			echo 'please fill all values';
		}else{
			

		$con=mysqli_connect("") or die("접속불가");

					$sql = "SELECT * FROM songDB WHERE userID='$userID' AND songName='$songName'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo '이미 같은 이름의 곡을 업로드 하셨습니다.';
			}else{				
$sql = "INSERT INTO songDB(username,userID,songName,contents,filepath) VALUES('$username','$userID','$songName','$contents','$filepath')";
	if(mysqli_query($con,$sql)){
		echo 'successfully Upload';
	}else{
		echo 'oops! Please try again!';
	}      


		mysqli_close($con);
}
}
?>
