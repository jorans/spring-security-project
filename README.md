# Test av Spring Security

Start applikationen med tex `./mvnw spring-boot:run`

Tillgängligheten av applikationens REST-tjänster styrs bla av roller och behörigheter och det finns några url:er där
olika metoder används för att begränsa tillgängligheten.

* `SecurityService` hanterar autenticering av användaren mot Spring Security
* `WebSecurityConfig` hanterar konfigurationen av auktoriseringen i Spring Security av REST-tjänsterna
* `AuthController` definierar REST-tjänsterna och vilka begränsningar de har i tillgänglighet via olika annoteringar
* `PermissionService` hanterar de behörigheter som olika roller får.

`AuthAccessService` och `AuthPermissionEvaluator` är hjälpklasser som används i annoteringarna för göra 
domänspecifika auktoriseringar. AuthAccessService slås upp i contextet av Spring Secucrity, så där är 
namngivningen av komponenten viktig, den autowiras inte in via controllern.

## Service
`IdService` har en deny-all-annotering på klassen som gör att metoderna måste explicit ange hur metoden 
ska skyddas. `IdService.getIds` har en annotering för efterbehandling av vart och ett av elementen i 
resultatet. `IdService.getId` delegerar auktoriseringen till en domänservice `AuthAccessService` som tar 
de inkommande argument som behövs för autktoriseringen.

## Tjänster
Login- och logout-tjänsterna kräver ingen autentisering av användaren (testsidan `/home2` är också oskyddad)
alla andra tjänster kräver en autentiserad användare.

Via login-tjänsten så autentiseras och auktoriseras en användare i contextet för Spring Security 
(informationen lagras i sessionen och görs tillgänglig via en ThreadLocal)
* [http://localhost:8080/login/admin](`http://localhost:8080/login/admin`)
* [http://localhost:8080/login/instructor](`http://localhost:8080/login/instructor`)
* [http://localhost:8080/login/student](`http://localhost:8080/login/student`)
* [http://localhost:8080/logout](`http://localhost:8080/logout`)

Logout invaliderar inloggning genom att tömma det Spring Security kontext som finns i sessionen och i tråden.

Via id-tjänsten kan man hämta information ett ID, vissa id:n är hemliga och kräver en speciell behörighet 
för att få hämta, `admin` har behörigheten `ReadSecretId` (alla ojämna id representerar ett hemligt ID)
* [http://localhost:8080/id/22](`http://localhost:8080/id/22`)
* [http://localhost:8080/id/23](`http://localhost:8080/id/23`)

I de fall man får tillbaka en list av ID:n så kan även filtrera listan så att den bara innehåller id:n som man är
behörig att se. Spring Security gör en separat behörighetskontroll av varje enskilt element och returnerar bara 
de där behörighetskontrollen var possitiv. I exmemplet ligger behörighetskontroller i domänservicen.
* [http://localhost:8080/ids/10](`http://localhost:8080/ids/10`)
`admin` har behörighet att läsa alla, medan `instructor` bara har rätt att läsa några, vilket hanteras av
behörighetsfiltret på servicemetoden.

Via private-tjänsten kan man hämta information om en användare, men man får bara hämta information om sig själv.
I första varianten uttrycks auktoriseringen direkt i annoteringen
* [http://localhost:8080/private/admin](`http://localhost:8080/private/admin`)
* [http://localhost:8080/private/instructor](`http://localhost:8080/private/instructor`)

I andra varianten implementeras auktoriseringen av en domänspecifik service som anges i annoteringen.
* [http://localhost:8080/private2/admin](`http://localhost:8080/private2/admin`)
* [http://localhost:8080/private2/instructor](`http://localhost:8080/private2/instructor`)



