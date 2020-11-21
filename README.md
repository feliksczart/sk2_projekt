# Opis projektu

Gra turowa w kółko i krzyżyk z interfejsem dla graczy (plansza 3x3).

Uczestniczy w niej n graczy, którzy będą podzieleni na 2 drużyny po równo, bądź z różnicą max 1 gracza. 
Gra się rozpoczyna, kiedy do serwera dołączy przynajmniej 2 klientów i oboje dadzą znać, że są gotowi (np. nacisną przycisk startu). 
W przeciwieństwie do klasycznych zasad w kółko i krzyżyk drużyna, która pierwsza wykonuje ruch, 
jest wybierana losowo (czyli grę może rozpocząć także drużyna X). 
W każdej kolejnej turze rozgrywki gracze z danej drużyny będą głosować na pole (z ograniczeniem czasowym 10s), 
w którym należy umieścić odpowiedni symbol. Jeśli gracz nie zagłosuje w wyznaczonym czasie, to jego głos przepada. 
Podczas głosowania gracz ma podgląd jedynie na to, ile osób z jego drużyny już zagłosowało, ale nie wie, na jakie pola oddano głosy. 
W przypadku remisu w głosowaniu algorytm umieści symbol w losowo wybranym polu, które otrzymało odpowiednią liczbę głosów.

Gracze mogą dołączać do rozgrywki w dowolnym momencie gry i nie mogą wybrać zespołu, do którego dołączają. Program przydzieli ich automatycznie. 
Jeśli gracz ulegnie awarii, gra jest kontynuowana bez niego. Jeśli podczas rozgrywki 1vs1 jeden z graczy ulegnie awarii, 
to w tym przypadku przy głosowaniu w drużynie bez graczy każde wolne pole będzie miało 0 głosów i serwer umieści symbol na losowo wybranym polu. 
Jeśli na serwerze podczas trwającej rozgrywki nie pozostanie żaden prawdziwy gracz, to serwer dokończy rozgrywkę, grając sam ze sobą 
i po zakończeniu trwającej partii będzie oczekiwał na przynajmniej 2 graczy.

Gdy któraś z drużyn stworzy linię trzech krzyżyków lub trzech kółek, to zwycięża. Gdy wszystkie pola są zajęte i nie zostanie utworzona linia, 
gra kończy się remisem. Jeśli rozgrywka się zakończy, przesyłamy o tym informację do klientów i serwer stworzy drużyny na nowo, 
losowo przydzielając jej członków i kolejność wykonywania ruchu drużyn.

Program gwarantuje również wsparcie dla dowolnej liczby gier na serwerze i automatyczne zamykanie gier,
w których w jednej z drużyn odłączą się wszyscy gracze.
