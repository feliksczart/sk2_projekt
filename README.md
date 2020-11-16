# sk2_projekt

Gra turowa w kółko i krzyżyk z interfejsem dla graczy.

W grze turowej będą obowiązywały zasady jak w grze w kółko i krzyżyk.
Uczestniczy w niej n graczy, którzy będą podzieleni na 2 drużyny po równo,
bądź z różnicą max 1 gracza. W każdej kolejnej turze rozgrywki gracze
z danej drużyny będą głosować na pole (z ograniczeniem czasowym 10s),
w którym należy umieścić odpowiedni symbol. Jeśli gracz nie zagłosuje
w wyznaczonym czasie, to jego głos przepada. Podczas głosowania gracz
nie ma podglądu na to, jaką decyzję podjęli inni członkowie jego drużyny.
W przypadku remisu w głosowaniu algorytm umieści symbol w losowo wybranym polu,
które otrzymało odpowiednią liczbę głosów.

Gracze mogą dołączać do gry w dowolnym momencie rozgrywki i nie mogą wybrać zespołu,
do którego dołączają. Program przydzieli ich automatycznie i co każdą nowo rozpoczętą
rozgrywkę będzie tworzył drużyny na nowo, losowo przydzielając jej członków.
Jeśli gracz ulegnie awarii, rozgrywka jest kontynuowana bez niego.
