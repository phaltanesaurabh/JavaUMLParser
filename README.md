# JavaUMLParser

JavaParser Program: 

The said program can be used for reverse engineering the Source code and generation of UML Diagrams. 
The Java Parser and YUML are the two tools used for the said project.   
Tools Used for Java Parser: 
Java Parser: Used for parsing the Java Code for generation of tokens that analysed are clubbed to form the YUML Language. 
YUML: YUML is the rendering software that generates the UML diagrams from the generated language from the parser.  

The UML diagrams in form of png images are stored in the source folder or as per the specified location and image name.  
 YUML: http://yuml.me  Java Parser : https://github.com/javaparser/javaparser  IDE: Eclipse  Java Version 1.8  

Libraries Used for Project: 
 Java Parser  Maven Dependencies  Java-json.jar 

Running the Project : 
 Java –jar  UMLparser.jar  <Location of Files> <outPutimageName(.png)> 
Eg:  
java -jar UMLparser.jar "C:/Users/saurabh-pc/Desktop/PaulJava/uml-parser-test-2/uml-parser-test2" "image.png" 

Note: The System is tested for Windows/Linux Environments. Internet Connection is necessary for program to run . (YUML  API ).    

Compilation of Code for Java  
 Extract the zipped Project and import to Project. 
 Import to Eclipse using Maven. 
 Build the Project.
 The yUML requires internet connection:  YUML: http://yuml.me 
 Important library used : Java Parser : https://github.com/javaparser/javaparser  

This parser parses the JAVA files and generates a UML Class diagram. 
Dependencies: 
.
Add below mentioned dependencies in pom.xml file of project.  
Dependencies: 
Add below mentioned dependencies in pom.xml file of project. 
 
1. <dependency>
    <groupId>com.github.javaparser</groupId> 
    <artifactId>javaparser-core</artifactId> 
    <version>2.1.0</version> 
  </dependency> 
 2. <dependency> 
    <groupId>org.json</groupId>
    <artifactId>json</artifactId> 
    <version>20150729</version> 
  </dependency> 
 3. <dependency> 
      <groupId>com.googlecode.json-simple</groupId> 
	  <artifactId>json-simple</artifactId> 
	  <version>1.1</version>
	</dependency>
Internet connection is required for fetching the class diagram image from yUML. 

















