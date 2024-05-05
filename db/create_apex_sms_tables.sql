CREATE TABLE ESB.SMS_APEX_CONFIG
(
  LAST_READ_TRANSACTION_ID  NUMBER              DEFAULT 0                     NOT NULL,
  IS_ENABLED                INTEGER             DEFAULT 1                     NOT NULL,
  KEEP_SMS_POOL             INTEGER             DEFAULT 1,
  ACCOUNTS_INCLUDE          VARCHAR2(500 CHAR)
);

Insert into ESB.SMS_APEX_CONFIG
(LAST_READ_TRANSACTION_ID, IS_ENABLED, KEEP_SMS_POOL, ACCOUNTS_INCLUDE)
Values
  (0, 1, 1, '2000000002104472129,2000000002308547371,2000000002404464149,2000000002434844583');
COMMIT;

CREATE TABLE ESB.SMS_APEX_TEMPLATES
(
  ID           NUMBER                           NOT NULL,
  ACTION_ID    NUMBER,
  DESCRIPTION  VARCHAR2(512 BYTE),
  TEMPLATE     VARCHAR2(240 BYTE)               NOT NULL,
  IS_ACTIVE    INTEGER                          DEFAULT 1                     NOT NULL
);

CREATE UNIQUE INDEX ESB.SMS_APEX_TEMPLATES_PK ON ESB.SMS_APEX_TEMPLATES
(ID);

ALTER TABLE ESB.SMS_APEX_TEMPLATES ADD (
CONSTRAINT SMS_APEX_TEMPLATES_PK
PRIMARY KEY
  (ID)
  USING INDEX ESB.SMS_APEX_TEMPLATES_PK
ENABLE VALIDATE);


Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (1, 10081, 'Безналичное внешнее пополнение основного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (2, 10091, 'Безналичное внутреннее пополнение основного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (3, 10111, 'Безналичное пополнение второго дополнительного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (4, 10111, 'Безналичное пополнение второго дополнительного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (5, 10051, 'Безналичное пополнение дополнительного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (6, 10011, 'Безналичное пополнение основного карточного счета', 'Vam zarahovano #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (7, 10121, 'Наличное пополнение второго дополнительного карточного счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (8, 10121, 'Наличное пополнение второго дополнительного карточного счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (9, 10061, 'Наличное пополнение дополнительного карточного счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (10, 10041, 'Наличное пополнение основного карточного счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (11, -1035, 'Пополнение второго доп. карт. счета (расходы)', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (12, -1038, 'Пополнение второго доп. карт. счета с транз. счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (13, -1031, 'Пополнение доп. карт. счета (расходы)', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (14, -1034, 'Пополнение доп. карт. счета с транз. счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (15, -1001, 'Пополнение осн. карт. счета (расходы)', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (16, -1004, 'Пополнение осн. карт. счета с транз. счета', 'Popovnennia rahunku na #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (17, -101, 'Возникновение задолженности по несанкц. перерасходу', 'Povidomliaemo pro zaborgovanist na vashomu rahunku. Detali 0800302088; +380443220082', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (18, -201, 'Возникновение задолженности по санкц. перерасходу', 'Povidomliaemo pro zaborgovanist na vashomu rahunku. Detali 0800302088; +380443220082', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (19, 1, 'Гашение задолженности по несанкц. перерасходу', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (20, 2, 'Гашение задолженности по процентам за несанкц. перерасход', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (21, 4, 'Гашение задолженности по процентам за санкц. перерасход', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (22, 3, 'Гашение задолженности по санкц. перерасходу', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (23, 11, 'Гашение платы за обслуж. карты', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (24, 16, 'Гашение платы за обслуживание', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (25, 8, 'Гашение просроченной задолженности по процентам за санкц. перерасход', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (26, 20, 'Гашение просроченной задолженности по процентам за санкц. перерасход свыше 30 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (27, 21, 'Гашение просроченной задолженности по процентам за санкц. перерасход свыше 60 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (28, 7, 'Гашение просроченной задолженности по санкц. перерасходу', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (29, 18, 'Гашение просроченной задолженности по санкц. перерасходу свыше 30 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (30, 19, 'Гашение просроченной задолженности по санкц. перерасходу свыше 60 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (31, 22, 'Гашение просроченной платы за обслуживание свыше 30 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (32, 23, 'Гашение просроченной платы за обслуживание свыше 60 дней', 'Pogasheno zaborgovanist'' u rozmiri #{amount} #{currency}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (33, 12, 'Гашение платы за обслуж. карт. счета', 'Richnu platu za paket poslug splacheno', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (34, 12121, 'Начисление годовой платы за обслуживания карточного счета', 'Vam narahovano richnu platu za paket poslug', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (35, 12111, 'Начисление ежемесячной платы за обслуживания карточного счета', 'Vam narahovano mis''achnu platu za paket poslug', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (36, 12021, 'Начисление платы за выпуск дополнительной карты', 'Vam narahovano platu za dodatkovu kartku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (37, 12061, 'Начисление платы за выпуск дубликата дополнительной карты', 'Vam narahovano platu za dodatkovu kartku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (38, 12051, 'Начисление платы за выпуск дубликата основной карты', 'Vam narahovano platu za kartku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (39, 12011, 'Начисление платы за выпуск основной карты', 'Vam narahovano platu za kartku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (40, 12101, 'Начисление платы за обслуживание дополнительной карты (годовая)', 'Vam narahovano platu za obslugovuvannya', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (41, 12081, 'Начисление платы за обслуживание дополнительной карты (ежемесячная)', 'Vam narahovano platu za obslugovuvannya', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (42, 12091, 'Начисление платы за обслуживание основной карты (годовая)', 'Vam narahovano platu za obslugovuvannya', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (43, 12071, 'Начисление платы за обслуживание основной карты (ежемесячная)', 'Vam narahovano platu za obslugovuvannya', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (44, 12041, 'Начисление платы за перевыпуск дополнительной карты', 'Vam narahovano platu za perevypusk kartky', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (45, 12321, 'Начисление платы за перевыпуск дополнительной карты (утеря/кража)', 'Vam narahovano platu za perevypusk kartky', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (46, 12031, 'Начисление платы за перевыпуск основной карты', 'Vam narahovano platu za perevypusk kartky', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (47, 12311, 'Начисление платы за перевыпуск основной карты (утеря/кража)', 'Vam narahovano platu za perevypusk kartky', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (48, -1037, 'Списание с второго доп. карт. счета (доходы)', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (49, -1036, 'Списание с второго доп. карт. счета на транз. счет', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (50, -1033, 'Списание с доп. карт. счета (доходы)', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (51, -1032, 'Списание с доп. карт. счета на транз. счет', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (52, -1006, 'Списание с осн. карт. счета на транз. счет с возможностью несанкц. п/р', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (53, -1005, 'Списание с осн. карт. счета на доходы с возможностью несанкц. п/р', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (54, -1003, 'Списание с осн. карт. счета (доходы)', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (55, -1002, 'Списание с осн. карт. счета на транз. счет', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (56, 10021, 'Cписание с карточного счета', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (57, 10071, 'Списание с дополнительного карточного счета', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (58, 10131, 'Списание с второго дополнительного карточного счета', 'Spysannia #{amount} #{currency} z kartkovogo rakhunku', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (59, 81621, 'Кредитовый слип по карте MasterCard в POS (domestic)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (60, 91621, 'Кредитовый слип по карте MasterCard в POS (domestic, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (61, 81631, 'Кредитовый слип по карте MasterCard в POS (international)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (62, 91631, 'Кредитовый слип по карте MasterCard в POS (international, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (63, 81611, 'Кредитовый слип по карте MasterCard в POS (local)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (64, 91611, 'Кредитовый слип по карте MasterCard в POS (local, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (65, 81601, 'Кредитовый слип по карте MasterCard в POS (on-us)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (66, 91601, 'Кредитовый слип по карте MasterCard в POS (on-us, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (67, 80621, 'Кредитовый слип по карте VISA в POS (domestic)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (68, 90621, 'Кредитовый слип по карте VISA в POS (domestic, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (69, 80631, 'Кредитовый слип по карте VISA в POS (international)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (70, 90631, 'Кредитовый слип по карте VISA в POS (international, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (71, 80611, 'Кредитовый слип по карте VISA в POS (local)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (72, 90611, 'Кредитовый слип по карте VISA в POS (local, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (73, 80601, 'Кредитовый слип по карте VISA в POS (on-us)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (74, 90601, 'Кредитовый слип по карте VISA в POS (on-us, друж.)', 'Poverneno #{amount} #{currency} #{merchant}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (75, 11311, 'Выплата начисленных процентов на второй дополнительный карточный счет  (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vam narahovano vidsotky na zalyshok #{amount} #{currency} za #{operMonth}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (76, 11111, 'Выплата начисленных процентов на дополнительный карточный счет  (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vam narahovano vidsotky na zalyshok #{amount} #{currency} za #{operMonth}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (77, 11091, 'Выплата начисленных процентов на карточный счет  (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vam narahovano vidsotky na zalyshok #{amount} #{currency} za #{operMonth}', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (78, -965, 'Выплата Кэш-бэк (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vash dohid za cashback sklav #{amount} #{currency}. Podatky splacheno', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (79, -920, 'Выплата Кэш-бэк (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vash dohid za cashback sklav #{amount} #{currency}. Podatky splacheno', 1);
Insert into ESB.SMS_APEX_TEMPLATES
(ID, ACTION_ID, DESCRIPTION, TEMPLATE, IS_ACTIVE)
Values
  (80, -902, 'Выплата Кэш-бэк (ВНИМАНИЕ! Сумма кэшбэка суммируется с %% на остаток!!! СМС должно уходить отдельно по %% на ост и отдельно по кэшбэк)', 'Vash dohid za cashback sklav #{amount} #{currency}. Podatky splacheno', 1);
COMMIT;

CREATE TABLE ESB.SMS_APEX_POOL
(
  PHONE    VARCHAR2(20 BYTE)                    NOT NULL,
  TEXT     VARCHAR2(255 CHAR)                   NOT NULL,
  IS_DONE  INTEGER                              DEFAULT 0                     NOT NULL,
  ID       NUMBER                               NOT NULL
);

CREATE UNIQUE INDEX ESB.SMS_APEX_POOL_PK ON ESB.SMS_APEX_POOL
(ID)
LOGGING
NOPARALLEL;

ALTER TABLE ESB.SMS_APEX_POOL ADD (
CONSTRAINT SMS_APEX_POOL_PK
PRIMARY KEY
  (ID)
  USING INDEX ESB.SMS_APEX_POOL_PK
ENABLE VALIDATE);

CREATE SEQUENCE ESB.SMS_APEX_QUEUE_SEQ
START WITH 1
MAXVALUE 9999999999999999999999999999
MINVALUE 0
NOCYCLE
NOCACHE
NOORDER;