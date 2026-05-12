To compile the project:
javac vn/edu/tdtu/edocument/*.java vn/edu/tdtu/edocument/model/*.java vn/edu/tdtu/edocument/service/*.java

To run the application:
java vn.edu.tdtu.edocument.MainSwingUI  


Get-ChildItem -Path . -Include *.class -Recurse | Remove-Item
javac vn/edu/tdtu/edocument/*.java vn/edu/tdtu/edocument/model/*.java vn/edu/tdtu/edocument/service/*.java
java vn.edu.tdtu.edocument.MainSwingUI