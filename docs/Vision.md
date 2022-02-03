
# Отслеживание популярных тем, постов и товаров в потоке данных на Apache Flink


## Назначение

Реализация эффективной по памяти версии аппаратора, который будет отслеживать наиболее популярные элементы в потоке данных. В проекте сравниваются два метода по производительности и скорости обработки потока данных. Решение должно обладать хорошей масштабируемостью и универсальностью.

## Принцип работы

Используя Apache Flink, оптимизируется отслеживание наиболее популярные элементы в потоке данных. Для этого используются запросы TOP-N. В программе запросы Top-N запрашивают N наибольших значений потоковой таблицым. В первом варианте реализации используется дефолтная реализация TOP-N. Во втором варианте - используются "sketches" (элементарные рандомизированные структуры данных). Для сравнения двух реализаций проводились тесты функционала, тесты  по времени и памяти для TOP-N (sketch vs default).

* Apache Flink — это платформа обработки потоков с открытым исходным кодом для распределенных приложений с высокой степенью отказоустойчивости и толерантностью к падениям.

## Пользователи продукта

Компании, разработчики, которым требуется получать популярные элементы из потока данных.

## Установка и настройка

Необоходиомо подключить

Apache Kafka

Apache Flink

Apache Cassandra

### Зависимости

kafka-flink(default impl topn)-cassandra

kafka-flink(sketch impl topn)-cassandra

## Использование

Подаем поток любых данных. Получаем базу данных с итоговыми результатами.