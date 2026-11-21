# ISEKAI — Progressione RPG (specifica tecnica)

## Versione iniziale del sistema

La progressione è server-authoritative e usa i dati persistenti del giocatore Minecraft. In questo modo il livello non dipende dal client e non richiede una libreria esterna.

| Campo | Valore iniziale |
|---|---|
| Chiave NBT radice | `isekai_progression` |
| Livello iniziale | 1 |
| Livello massimo della prima implementazione | 100 |
| Esperienza iniziale | 0 |
| Ricompensa Sentinella dell'Aether | 45 PE |
| PE richiesti dal livello 1 al 2 | 100 |
| Incremento del costo per livello | +50 PE |

Formula iniziale:

`PE richiesti = 100 + (livello - 1) × 50`

L'esperienza eccedente viene conservata quando si supera più di un livello con una singola ricompensa. Al livello massimo l'esperienza residua viene azzerata.

## Eventi

- `LivingDeathEvent`: assegna esperienza al giocatore che ha ucciso una Sentinella dell'Aether.
- `PlayerEvent.Clone`: copia i dati di progressione quando il giocatore viene ricreato.
- `RegisterCommandsEvent`: registra `/isekai status`.

## Evoluzione prevista

1. Estendere le ricompense a tutte le famiglie di creature, con valori definiti nel catalogo.
2. Introdurre statistiche primarie e secondarie con salvataggio persistente.
3. Collegare livelli, classi, razze, abilità e requisiti dell'equipaggiamento.
4. Aggiungere interfaccia dedicata e schermate di progressione.
5. Aggiungere configurazione server per curva PE e ricompense.

Le fasi successive dovranno mantenere la compatibilità con i dati già salvati: le chiavi NBT esistenti non vanno rinominate senza una migrazione.
