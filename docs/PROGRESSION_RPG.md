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


## Prima fetta dei sistemi di gioco

Questa fase adotta uno sviluppo orizzontale: ogni grande area riceve una piccola funzione verificabile prima dell'espansione completa.

### Identità: razze e classi

Comandi:
- `/isekai profile`: visualizza il profilo.
- `/isekai race choose <razza>`: sceglie una razza tra `human`, `elf`, `dwarf`, `demonkin`.
- `/isekai class choose <classe>`: sceglie una classe tra `warrior`, `mage`, `ranger`.

La prima scelta è persistente e non modificabile tramite questi comandi. In questa build razza e classe sono identità registrate, non hanno ancora alberi evolutivi o bonus completi. La scelta definitiva di bilanciamento sarà fatta dopo la prova in gioco.

### Missione e ricompense

Missione iniziale: **Caccia alle Sentinelle**.
- Obiettivo: sconfiggere 3 Sentinelle dell'Aether.
- Ricompensa: 75 monete e 150 PE.
- Avanzamento automatico alla morte della Sentinella.
- Comando `/isekai quest` per vedere l'obiettivo.
- Comando `/isekai wallet` per controllare le monete.
- Completamento e ricompensa sono salvati nei dati persistenti.

### Abilità attiva

**Esplosione di Aether**:
- Comando: `/isekai skill use aether_burst`.
- Costo: 20 mana.
- Danno: 6 danni magici per bersaglio.
- Area: raggio di 4 blocchi intorno al giocatore.
- Ricarica: 8 secondi.
- Colpisce le creature viventi non alleate presenti nell'area.
- `/isekai skill list` mostra le abilità disponibili.

È un prototipo di abilità ad area: animazioni dedicate, progressione dei gradi, requisiti di classe e bilanciamento avanzato sono ancora da realizzare.

### Economia iniziale

La valuta persistente è la moneta ISEKAI. Al momento viene assegnata dalla missione iniziale; negozi, mercanti NPC, prezzi e commercio saranno collegati in una fase successiva.
