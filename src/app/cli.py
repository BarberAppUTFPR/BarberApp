from __future__ import annotations

import json

from .tasks import factorial, is_palindrome, run_tasks, sum_numbers


def main() -> None:
    tasks = [
        lambda: sum_numbers([1, 2, 3, 4, 5]),
        lambda: is_palindrome("Socorram-me, subi no ônibus em Marrocos"),
        lambda: factorial(10),
    ]
    results = run_tasks(tasks)
    print(json.dumps(results, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
