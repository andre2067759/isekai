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
| Spirito (`spirit`) | Riservata a mana, energia e tecniche spirituali | 50 |

I modificatori degli attributi vanilla vengono rimossi e riapplicati usando UUID stabili, evitando duplicazioni quando il giocatore rientra o rinasce.

## Comandi

- `/isekai status`: mostra livello, esperienza, punti disponibili e tutte le statistiche.
- `/isekai allocate <stat> <punti>`: spende punti in una statistica.
- Valori ammessi per `<stat>`: `strength`, `vitality`, `agility`, `intelligence`, `spirit`.
- Il comando rifiuta statistiche oltre il limite o spese superiori ai punti disponibili.

## Ricompense attuali

La Sentinella dell'Aether assegna 45 PE al giocatore che infligge il colpo finale. La ricompensa è una prima integrazione dimostrativa; il catalogo completo delle creature definirà in seguito valori e regole di ricompensa per ogni famiglia.

## Compatibilità e migrazione

I nuovi campi vengono inizializzati a zero quando un vecchio salvataggio non li contiene. Le chiavi `level` ed `experience` già introdotte restano invariate. Nessuna dipendenza obbligatoria da mod esterne.
