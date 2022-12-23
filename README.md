# WhatsTheWeatherRepository

Il progetto offre la possibilità di recuperare le previsioni meteo per i prossimi 5 giorni relativi ad una località specifica. 
L'app in fase di apertura chiede i permessi per accedere alla posizione dell'utente: 

- In caso di permesso accettato, si avranno le previsioni del meteo per la località in cui si trova l'utente
- In caso di permesso negato, l'utente viene indirizzato alla schermata in cui effettuare la ricerca di una località

Il flusso "completo" del progetto si compone di tre step: 

- schermata iniziale con previsioni della località in cui si trova l'utente 
- al tap sull'icona "cerca", si viene indirizzati alla schermata di ricerca località
- al tap su una località si aprirà la schermata di dettaglio delle previsioni 

Il progetto supporta anche la dark mode e riconosce automaticamente se questa è attiva o meno sul device utilizzato. 
