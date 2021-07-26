# "Astra"-nomy Spring-Boot Java Starter Application powered by DataStax Astra.
</br>
This code provides a starter Springboot Maven Java App with all the necessary Jars and examples to build Astra based applications using REST, Document, GraphQL and CQL APIs. You can start off your development with this App
</br>

<h2> Set Up: </h2>

<h3> Pre-requisites: </h3>
<ul>
<li> Setup Astra at astra.datastax.com </li>
<li> Click on the Dashboard to get the Database connectivity details (Region and ClusterID) as shown below : </li> 
</br>

![Screen Shot 2021-07-26 at 10 00 53 AM](https://user-images.githubusercontent.com/71985367/127029190-aa5847b3-eebf-4f1b-b4bb-43da1a8bc4ec.png)
</br>
<li> Generate User Token (if you don't already have one) 
Please generate a User token to connect to Astra Database to perform DDL and DML operations : 
</br> 
<p> On the top left corner of the Astra home page, click :  </br>
"Current Organization" --> "Organization Settings" --> "Token Management" --> "Select a Role" and click Generate Token. </p> 

You should see something like this :
</br>
</li>

![Screen Shot 2021-07-26 at 10 53 58 AM](https://user-images.githubusercontent.com/71985367/127035608-33df2f46-4eeb-45e6-8ed1-d2bf61160227.png)



<li> Download the Token details to a location on your desk top for future reference (this is a critical step) </li>
<li> Now you are good to move forward with development ... Yay !!! </li>
</ul>

<h3> Development : Start off your application development with this Spring Boot Starter App </h3>
<ul>
  <li> Download this Code </li>
  <li> Replace the following Astra connection details using your downloaded Token 
      <ul>
        <li> database-id: --------------- </li>
        <li> client-id: --------------- </li>
        <li> client-secret: --------------- </li>
        <li> application-token: --------------- </li>
        <li> cloud-region: --------------- </li>
        <li> keyspace: --------------- </li>
      </ul>
  </li>
  <li> Test your connection using one of the test classes </li>
</ul>
