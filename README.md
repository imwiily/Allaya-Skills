# 🧠 AllayaSkills

> Um sistema completo de **habilidades passivas evolutivas**, feito para servidores PvP e PvE no Minecraft 1.21.4.  
> Com caminhos de progressão, árvore de skills, armazenamento em SQLite e suporte a regiões do WorldGuard.

---

## ✨ Funcionalidades

- 🌿 Árvore de habilidades passivas desbloqueáveis com pontos
- 🛣️ Caminhos evolutivos (ex: `combate`, `mineracao`)
- ⛏️ XP baseado em ações (matar, minerar, etc)
- 🔐 Limitações configuráveis por:
    - Região (WorldGuard)
    - Mundo
    - Ferramenta usada
    - Permissão
    - Chance de ativação
- 🧠 Sistema de pontos por nível alcançado
- 💾 Banco de dados local via SQLite
- 🧩 Placeholders integrados com PlaceholderAPI
- 📦 GUI customizável via YAML (estilo CommandPanels, sem dependências externas)

---

## 🧱 Requisitos

- Minecraft `1.21.4`
- Java `21`
- Plugins recomendados:
    - [WorldGuard](https://enginehub.org/worldguard/)
    - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

---

## 📂 Exemplo de `paths.yml`

```yaml
paths:
  combate:
    display-name: "Caminho do Combate"
    xp-source: PLAYER_KILL
    xp-per-event: 10
    starting-xp: 100
    xp-increase-per-level: 50
    points-per-level: 1

  mineracao:
    display-name: "Caminho da Mineração"
    xp-source: BLOCK_BREAK
    xp-per-event: 5
    starting-xp: 80
    xp-increase-per-level: 40
    points-per-level: 1
    xp-blocks:
      STONE: 1
      COAL_ORE: 3
      IRON_ORE: 5
      GOLD_ORE: 6
      DIAMOND_ORE: 10
      NETHERITE_ORE: 15
```

---

## 📘 Exemplo de skills.yml

```yaml
maos_rapidas:
  name: "Mãos Rápidas"
  description: "Aumenta a velocidade de mineração em 10%."
  path: "mineracao"
  unlock_level: 0
  skill-points-required: 1
  requires: []
  region-allowed: []
  cooldown: 0
  effects:
    - trigger: PASSIVE_TICK
      effect: POTION_EFFECT
      type: FAST_DIGGING
      amplifier: 0
      duration: 40

sorte_minerador:
  name: "Sorte dos Mineradores"
  description: "25% de chance de dropar o minério duas vezes."
  path: "mineracao"
  unlock_level: 1
  skill-points-required: 2
  requires:
    - maos_rapidas
  region-allowed: []
  cooldown: 0
  effects:
    - trigger: BLOCK_BREAK
      effect: DOUBLE_DROP
      chance: 0.25
      blocks:
        - COAL_ORE
        - IRON_ORE
        - GOLD_ORE
        - DIAMOND_ORE
        - NETHERITE_ORE

mineracao_explosiva:
  name: "Mineração Explosiva"
  description: "Quebra uma área de 3x3 ao minerar minérios (10s de cooldown)."
  path: "mineracao"
  unlock_level: 2
  skill-points-required: 2
  requires:
    - maos_rapidas
  region-allowed:
    - mina_normal
    - mina_vip
  cooldown: 10
  required-permission: "skills.use.mineracao_explosiva"
  effects:
    - trigger: BLOCK_BREAK
      effect: AREA_BREAK
      radius: 1
      shape: CUBE
      chance: 0.5
      condition:
        blocks:
          - COAL_ORE
          - IRON_ORE
          - GOLD_ORE
          - DIAMOND_ORE
          - NETHERITE_ORE
        tools:
          - IRON_PICKAXE
          - DIAMOND_PICKAXE
        worlds:
          - minas
```

---

## 🖥️ Comandos

```yaml
/skills                                     - Abre o menu de caminhos
/skills open <caminho>                      - Abre diretamente a árvore do caminho
/skills invest <skill-id>                   - Tenta desbloquear uma skill
/skills points                              - Mostra pontos disponíveis
/skills info <jogador>                      - Mostra progresso de outro jogador
/skills givexp <jogador> <path> <qtd>       - Adiciona XP (admin)
/skills givepoints <jogador> <path> <qtd>   - Adiciona pontos (admin)
/skills reset <jogador> <path|all>          - Reseta progresso (admin)

```

---

## 🔐 Permissões

```yaml
skilltree.use                  - Usar o sistema de skills
skilltree.admin                - Acesso a comandos administrativos
skilltree.reset.self           - Permite resetar o próprio progresso
skilltree.view.self            - Ver seu próprio progresso detalhado
```

---

## 🧩 Placeholders (PlaceholderAPI)

```yaml
%allayaskills_level_<path>%         - Nível no caminho
%allayaskills_xp_<path>%            - XP atual
%allayaskills_xp_needed_<path>%     - XP necessário pro próximo nível
%allayaskills_points_<path>%        - Pontos disponíveis
%allayaskills_has_skill_<id>%       - true/false se o jogador tem a skill

```

---

## 💾 Banco de Dados (SQLite)

```sql
-- Progresso por caminho
CREATE TABLE player_skills (
  uuid TEXT NOT NULL,
  path TEXT NOT NULL,
  level INTEGER,
  current_xp INTEGER,
  xp_needed INTEGER,
  points_earned INTEGER,
  points_used INTEGER,
  PRIMARY KEY (uuid, path)
);

-- Habilidades desbloqueadas
CREATE TABLE player_unlocked_skills (
  uuid TEXT NOT NULL,
  skill_id TEXT NOT NULL,
  path TEXT NOT NULL,
  PRIMARY KEY (uuid, skill_id)
);
```

---

## 📦 Estrutura do Plugin

```arduino
AllayaSkills/
├── config.yml
├── skills.yml
├── paths.yml
├── gui-combate.yml
├── gui-mineracao.yml
├── database.db

```

## ✅ Futuras Funcionalidades Planejadas

- [ ] Especializações (ex: Gladiador, Guardião)
- [ ] Skills ativas com cooldown
- [ ] Comando de ranking: `/skills top <path>`
- [ ] Integração com MySQL (opcional ao SQLite)
- [ ] Sistema de recompensas visuais por nível (title, sound, partículas)
- [ ] Eventos temporários de XP (ex: double XP por tempo limitado)

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Envie um pull request com melhorias, novas skills, ou integrações.

---

## 📜 Licença

Este projeto está licenciado sob a MIT License.