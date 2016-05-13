
<?php
define('HOST','####');
define('USER','####');
define('PASS','####');
define('DB','####');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 

$sql = "select * from songDB";
$number=1;

$result = mysqli_query($con,$sql); 
while($row = mysqli_fetch_row($result))
{
echo ':::'.$row[1];
echo ':::'.$row[2];
echo ':::'.$row[3];
echo ':::'.$row[4];
echo ':::'.$row[5];
echo ':::'.$row[6];
echo '--:--'; 
$number++;
}
 
mysqli_close($con);
?>
