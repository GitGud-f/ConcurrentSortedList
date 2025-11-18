# Concurrent Sorted Linked List

## Introduction
This repository contains the implementation of a concurrent sorted linked list in Java, designed to be thread-safe while maintaining performance under multi-threaded access. The project explores synchronization techniques to handle data races and ensure mutual exclusion, as part of a homework assignment for a parallel computing course at the Higher Institute for Applied Sciences and Technology (HIAST).

The list supports add, remove, and contain operations, ensures no duplicates, and remains sorted in ascending order. Sentinel nodes (Integer.MIN_VALUE and Integer.MAX_VALUE) are used to simplify boundary handling. Three implementations are provided: coarse-grained synchronization using `synchronized` and `ReentrantLock`, and read-write locking with `ReentrantReadWriteLock` (both coarse- and fine-grained via object-based isolation with hand-over-hand locking).

The goal is to demonstrate and benchmark different locking strategies to understand their impact on concurrency and performance.

## Content
- **Data Race Analysis**: Discussion on potential races in unsynchronized lists and how synchronization prevents them.
- **Mutual Exclusion**: Ensuring exclusive access during modifications.
- **Structured Lock (synchronized)**: Simple coarse-grained locking using Java's built-in `synchronized` keyword.
- **Unstructured Lock (ReentrantLock, ReentrantReadWriteLock)**: Explicit locking for more control, including read-write separation and fine-grained per-node locks.
- **Testing Different Implementations' Performance**: A benchmark framework to measure execution times, list sizes, and success rates for add, contain, and remove operations under concurrent threads.
- **Test Time for Different Sorted Linked List Implementations**: Results from running benchmarks with 50,000 operations, 7 add threads, 8 contain/remove threads, using a shared random sequence (seed=0, range=80,000).

## Features
- Thread-safe operations with correctness verified by consistent benchmark metrics.
- Bug fixes to base code (e.g., redundant checks, typos).
- Added `getSize()` method for thread-safe list length computation.
- Success counters in test threads for detailed metrics.
- Two benchmark runs: one with coarse-grained RWLock, one with fine-grained (object-based isolation).

## Setup
### Requirements
- Java JDK 8 or higher.
- JUnit for testing (optional, as benchmarks run standalone).

### Installation
1. Clone the repository:
```bash
  git clone https://github.com/k3rnel-paN1c5/ConcurrentSortedList.git
```
2. Navigate to the project directory:
``` bash
    cd concurrentsortedlinkedlist
```
3. Compile the source files:
``` bash
    javac src/.java test/.java
```
## Usage
Run the benchmarks using the `SyncListTest` class

Invoke the `testRun()` method (or run via IDE/JUnit). This will execute benchmarks for SyncList, RWLockList (configurable as coarse or fine-grained), and LockList, printing results to the console.

## Benchmark Results
Benchmarks were run twice: once with coarse-grained RWLockList and once with fine-grained (object-based isolation).

### First Run (Coarse-Grained RWLockList)
- **SyncList**:
    - Add: 4036 ms, List length: 31851
    - Contain: 8064 ms, Successes: 16282, Failures: 24219
    - Remove: 6268 ms, List length: 19212, Successes: 12639, Failures: 27862
- **RWLockList (Coarse)**:
    - Add: 4712 ms, List length: 31851
    - Contain: 1112 ms, Successes: 16282, Failures: 24219
    - Remove: 1624 ms, List length: 19212, Successes: 12639, Failures: 27862
- **LockList**:
    - Add: 4256 ms, List length: 31851
    - Contain: 8302 ms, Successes: 16282, Failures: 24219
    - Remove: 7206 ms, List length: 19212, Successes: 12639, Failures: 27862

### Second Run (Fine-Grained RWLockList)
- **SyncList**:
    - Add: 5710 ms, List length: 31851
    - Contain: 11336 ms, Successes: 16282, Failures: 24219
    - Remove: 8245 ms, List length: 19212, Successes: 12639, Failures: 27862
- **RWLockList (Fine)**:
    - Add: 2370 ms, List length: 31851
    - Contain: 3427 ms, Successes: 16282, Failures: 24219
    - Remove: 3293 ms, List length: 19212, Successes: 12639, Failures: 27862
- **LockList**:
    - Add: 5431 ms, List length: 31851
    - Contain: 11480 ms, Successes: 16282, Failures: 24219
    - Remove: 8269 ms, List length: 19212, Successes: 12639, Failures: 27862

Observations: Fine-grained locking improves add/remove times due to reduced contention, while coarse-grained RWLock excels in read-heavy contains.
## Report
A detailed report is included in `report.pdf`, covering implementation, analysis, and answers to discussion questions.
## References
[Concurrent Programming in Java](https://www.coursera.org/learn/concurrent-programming-in-java/home/week/1)

[Locking Java](https://web.archive.org/web/20200809031812/http://www.ibm.com/developerworks/java/library/j-jtp10264)