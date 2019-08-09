I used an MVVM architecture for the project and also implemented Dagger 2 as well. I applied retrofit in 
order to make the API call to retrieve the JSON object containing the pin data. I used SQLite along with the Room 
Persistence Library to store the pin data. I used other libraries like RxJava (Flowables, observers, subscribers, 
and observables) as well. I used a Flowable to encapsulate the retrofit object and I retrieved data (SQLite and retrofit data) 
from my viewModel class using  the CompositeDisposable class. When the app opens your current location will appear. 
Sliding up the panel from the bottom labeled locations displays the recyclerView list containing all the locations 
from the JSON object. There is also a dropdown button in each row revealing the description for each location. 
When you click on a location in the recyclerView, the mapbox map moves to its latitude and longitude putting a marker on it. 
The start Navigation button is enabled and when that is clicked the app will navigate you to the location that was tapped. I
applied Java and Kotlin to this project and I also applied unit testing utilizing Mockito, junit 4, junit 5 and espresso. 
