from __future__ import annotations

import re
from collections.abc import Callable
from time import perf_counter
from typing import Any


def sum_numbers(nums: list[int]) -> int:
    return sum(nums)


def is_palindrome(s: str) -> bool:
    normalized = re.sub(r"[^a-z0-9]", "", s.lower())
    return normalized == normalized[::-1]


def factorial(n: int) -> int:
    if n < 0:
        raise ValueError("n must be >= 0")
    result = 1
    for i in range(2, n + 1):
        result *= i
    return result


def run_tasks(task_fns: list[Callable[[], Any]]) -> list[dict[str, Any]]:
    results: list[dict[str, Any]] = []
    for fn in task_fns:
        start = perf_counter()
        value = fn()
        elapsed = perf_counter() - start
        results.append({"task": fn.__name__, "result": value, "seconds": round(elapsed, 6)})
    return results
