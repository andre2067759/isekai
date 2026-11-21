# ISEKAI — Progressione RPG e statistiche

## Progressione

- Livello iniziale: 1.
- Livello massimo attuale: 100.
- Esperienza persistente nei dati del giocatore.
- Ricompensa iniziale: 45 PE per sconfiggere una Sentinella dell'Aether.
- Formula: PE richiesti = 100 + (livello - 1) × 50.
- Ogni livello ottenuto assegna 1 punto statistica.
- I punti e le statistiche sono salvati nel tag persistente `isekai_progression`.

## Statistiche primarie

| Statistica | Effetto attuale per punto | Limite |
|---|---|---:|
| Forza (`strength`) | +0,5 danni d'attacco | 50 |
| Vitalità (`vitality`) | +2 punti salute massima | 50 |
| Agilità (`agility`) | +0,0015 velocità di movimento | 50 |
| Intelligenza (`intelligence`) | Riservata al sistema magico | 50 |
| Spirito (`spirit`) | +10 mana massimo e +0,1 mana/secondo | 50 |

I modificatori degli attributi vanilla vengono rimossi e riapplicati usando UUID stabili, evitando duplicazioni quando il giocatore rientra o rinasce.

## Mana e risorse magiche

- Mana iniziale: 100.
- Intelligenza aumenta la riserva massima di 5 punti per punto statistica.
- Spirito aumenta la riserva massima di 10 punti e la rigenerazione di 0,1 mana al secondo per punto.
- Rigenerazione base: 1 mana al secondo.
- La riserva e il valore corrente sono persistenti e vengono mantenuti entro il massimo consentito.

## Comandi

- `/isekai status`: mostra livello, esperienza, punti disponibili, statistiche e mana.
- `/isekai mana`: mostra la riserva di mana corrente e massima.
- `/isekai allocate <stat> <punti>`: spende punti in una statistica.
- Valori ammessi per `<stat>`: `strength`, `vitality`, `agility`, `intelligence`, `spirit`.
- Il comando rifiuta statistiche oltre il limite o spese superiori ai punti disponibili.

## Ricompense attuali

La Sentinella dell'Aether assegna 45 PE al giocatore che infligge il colpo finale. La ricompensa è una prima integrazione dimostrativa; il catalogo completo delle creature definirà in seguito valori e regole di ricompensa per ogni famiglia.

## Compatibilità e migrazione

I nuovi campi vengono inizializzati a zero quando un vecchio salvataggio non li contiene. Le chiavi `level` ed `experience` già introdotte restano invariate. Nessuna dipendenza obbligatoria da mod esterne.


## Primo incantesimo: Dardo di Aether

| Proprietà | Valore |
|---|---|
| ID | `isekai:aether_bolt` |
| Scuola | Aether |
| Costo | 25 mana |
| Danno base | 12 danni magici |
| Portata | 18 blocchi |
| Ricarica | 40 tick (2 secondi) |
| Bersaglio | Prima creatura vivente intercettata lungo la linea di mira |
| Ostacoli | Il tracciamento si arresta al primo blocco solido |

Comando provvisorio: `/isekai cast aether_bolt`.

L'incantesimo consuma mana solo quando colpisce e danneggia un bersaglio. La ricarica viene salvata nei dati persistenti del giocatore. L'attivazione tramite comando è temporanea: il sistema sarà successivamente collegato a selezione incantesimi, tasti rapidi e interfaccia dedicata.
