#!/usr/bin/python3

import seaborn as sns
import argparse
import json
import matplotlib.pyplot as plt
import re
import pandas as pd
import os
import sys
import pathlib
from pathlib import Path


def parse_hyperfine_result_file(path):
    with open(str(path)) as f:
        results = json.load(f)["results"]

    assert(len(results) == 1)
    cmd = results[0]["command"]
    times = results[0]["times"]
    assert(len(times) == 4)

    m = re.match(
        r".*/(?P<stem>.*)__STEM_(?P<backend>compile|compile-firm)__(?P<optimization>.+)__", cmd)
    if m is None:
        raise Exception("unexpected command name: " + cmd)

    measurements = []
    for time in times:
        measurements.append({
            "bench": m.group("stem"),
            "backend": m.group("backend"),
            "optimization": m.group("optimization"),
            "time": time,
        })
    return measurements


os.chdir(os.path.dirname(sys.argv[0]))


p = argparse.ArgumentParser(description='Process ./run.bash results')
p.add_argument('action', choices=['show', 'pdf'])
p.add_argument('--resultsdir', default="./results/")
args = p.parse_args()

results = []
resultsdir = Path(args.resultsdir)
for resultfilepath in resultsdir.glob("*HYPERFINE*.json"):
    results.extend(parse_hyperfine_result_file(resultfilepath))

sns.set()

df = pd.DataFrame.from_dict(results)

bench_order = list(df["bench"])
bench_order = sorted(list(set(bench_order)))
print(bench_order)

optimization_order = list(df["optimization"])
optimization_order = sorted(list(set(optimization_order)))
print(optimization_order)

fig, axes = plt.subplots(2, 1, sharey=True)
fig.set_size_inches(30, 20)

for (i, show_backend) in enumerate(["compile", "compile-firm"]):
    ax = axes[i]
    ax.set_ylabel(show_backend)
    ax.set_title(show_backend)
    show = df[df['backend'] == show_backend]
    print(show)
    sns.barplot(x="bench", y="time", hue='optimization',
                data=show, ax=ax, order=bench_order, hue_order=optimization_order)

plt.tight_layout()

if args.action == "show":
    plt.show()
elif args.action == "pdf":
    fig.savefig(str(resultsdir / 'summary.pdf'), bbox_inches='tight')
else:
    raise Exception("unknown action " + args.action)
