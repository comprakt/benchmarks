#!/bin/bash
set -eu

cd $(dirname "${BASH_SOURCE[0]}")

mkdir -p results

resultsdir=$(readlink -f results)
benchsdir=$(readlink -f .)
scriptdir=$(readlink -f .)

cd ../comprakt

BENCHES=()
BENCHES+=("comprakt_bench_load_store.INLINEUTILS")
BENCHES+=("bench_math.input")
BENCHES+=("bench_fannkuchredux")
BENCHES+=("bench_conways_game_of_life.input")
BENCHES+=("bench_BigTensorProduct.input")

BACKENDS=()
BACKENDS+=("compile")
BACKENDS+=("compile-firm")

OPTIMIZATIONS=()
OPTIMIZATIONS+=("none")
OPTIMIZATIONS+=("moderate")
OPTIMIZATIONS+=("aggressive")

BINARIES=""
for bench in $BENCHES; do
    bench_orig="${benchsdir}/${bench}.java"
    if [[ "$bench" =~ .*INLINEUTILS.* ]]; then
        ${scriptdir}/inline_mjtest_exec_utils.sh "$bench_orig"
        bench_input="${bench_orig}.inlined.java"
    else
        bench_input="${bench_orig}"
    fi
    for backend in $BACKENDS; do
        for optimization in $OPTIMIZATIONS; do
            stem="${resultsdir}/${bench}__STEM_${backend}__${optimization}__"
            cargo run -- --${backend} -O ${optimization} --emit-asm "$stem".S -o "$stem" "$bench_input"
            BINARIES="$BINARIES $stem"
        done
    done
done

for bin in $BINARIES; do
    echo run $bin
    # reconstruct benchmark name from binary name
    bench="${bin%%__STEM*}"
    bench=$(basename "$bench")
    hyperfine_cmd="hyperfine -r 4 --export-json "
    if [[ "$bench" =~ .*input ]]; then
        without_input="${bench%.input}"
        for input in ${benchsdir}/${without_input}*inputc ; do
            echo "$input"
            input_base=$(basename "$input")
            $hyperfine_cmd "${stem}__HYPERFINE_WITH_INPUT_${input_base}.json" "$bin < $input"
        done
    else
        $hyperfine_cmd "${stem}__HYPERFINE_NO_INPUT.json" "$bin"
    fi
done