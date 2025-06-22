# ORM - Calculadora Veterinária

Este projeto implementa o mapeamento objeto-relacional (ORM) utilizando JPA com Spring Boot para um sistema de cálculo de dosagens veterinárias. As entidades representam os principais componentes do sistema e seus relacionamentos.

## Entidades principais

- **Usuario**: Representa os usuários do sistema, contendo dados pessoais e credenciais.
- **PasswordResetToken**: Relacionada ao `Usuario`, usada para recuperação de senha via token temporário.
- **Animal**: Representa o animal a ser tratado, com peso, espécie e raça associados.
- **Especie**: Define a espécie do animal (ex: Canina, Felina), com relação a várias raças.
- **Raca**: Define a raça do animal, relacionada a uma espécie.
- **Medicamento**: Contém o nome e a dosagem padrão por quilo.
- **Calculo**: Registra um cálculo de dosagem feito com base no peso do animal e no medicamento.

## Relacionamentos

- `Usuario` → `PasswordResetToken`: **1:N**
- `Especie` → `Raca`: **1:N**
- `Raca` → `Animal`: **1:N**
- `Especie` → `Animal`: **1:N**
- `Animal` → `Calculo`: **1:N**
- `Medicamento` → `Calculo`: **1:N**

Todos os relacionamentos foram mapeados com as anotações JPA (`@ManyToOne`, `@OneToMany`) de forma adequada para persistência em banco de dados relacional.

