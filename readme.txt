To compile the project:
javac vn/edu/tdtu/edocument/*.java vn/edu/tdtu/edocument/model/*.java vn/edu/tdtu/edocument/service/*.java

To run the application:
java vn.edu.tdtu.edocument.MainSwingUI  


Get-ChildItem -Path . -Include *.class -Recurse | Remove-Item
javac -d . vn/edu/tdtu/edocument/*.java vn/edu/tdtu/edocument/model/*.java vn/edu/tdtu/edocument/service/*.java vn/edu/tdtu/edocument/service/strategy/*.java vn/edu/tdtu/edocument/service/validation/*.java vn/edu/tdtu/edocument/service/notification/*.java vn/edu/tdtu/edocument/service/persistence/*.java
java vn.edu.tdtu.edocument.MainSwingUI    