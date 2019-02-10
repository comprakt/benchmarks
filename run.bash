#!/bin/bash
set -eu

cd $(dirname "${BASH_SOURCE[0]}")

mkdir -p results

resultsdir=$(readlink -f results)
benchsdir=$(readlink -f .)
scriptdir=$(readlink -f .)

cd ../comprakt

BENCHES=()
BENCHES+=("bench_math.input")
BENCHES+=("bench_fannkuchredux")
BENCHES+=("bench_conways_game_of_life.input")
BENCHES+=("bench_BigTensorProduct.input")
BENCHES+=("comprakt_bench_safe_arrays")
BENCHES+=("comprakt_bench_linear_scan.INLINEUTILS.input")
BENCHES+=("comprakt_bench_load_store.INLINEUTILS")


BACKENDS=()
BACKENDS+=("compile")
BACKENDS+=("compile-firm")

OPTIMIZATIONS=()
OPTIMIZATIONS+=("none")
OPTIMIZATIONS+=("Custom:Inline,ConstantFolding,ControlFlow,NodeLocal")
OPTIMIZATIONS+=("Custom:Inline,ConstantFoldingWithLoadStore,ControlFlow,NodeLocal")
OPTIMIZATIONS+=("Custom:Inline,ConstantFolding,ControlFlow")
OPTIMIZATIONS+=("Custom:Inline,ConstantFoldingWithLoadStore,ControlFlow")
OPTIMIZATIONS+=("Custom:ConstantFolding,ControlFlow,NodeLocal")
OPTIMIZATIONS+=("Custom:ConstantFoldingWithLoadStore,ControlFlow,NodeLocal")
OPTIMIZATIONS+=("Custom:ConstantFolding,ControlFlow")
OPTIMIZATIONS+=("Custom:ConstantFoldingWithLoadStore,ControlFlow")

BINARIES=()
for bench in "${BENCHES[@]}"; do
    bench_orig="${benchsdir}/${bench}.java"
    if [[ "$bench" =~ .*INLINEUTILS.* ]]; then
        ${scriptdir}/inline_mjtest_exec_utils.sh "$bench_orig"
        bench_input="${bench_orig}.inlined.java"
    else
        bench_input="${bench_orig}"
    fi
    for backend in "${BACKENDS[@]}"; do
        for optimization in "${OPTIMIZATIONS[@]}"; do
            stem="${resultsdir}/${bench}__STEM_${backend}__${optimization}__"
            cargo run -- --${backend} -O ${optimization} --emit-asm "$stem".S -o "$stem" "$bench_input" && BINARIES+=("$stem")
        done
    done
done

for bin in "${BINARIES[@]}"; do
    echo run $bin
    # reconstruct benchmark name from binary name
    bench="${bin%%__STEM*}"
    bench=$(basename "$bench")
    jsonprefix="${resultsdir}/"$(basename "$bin")"__HYPERFINE_"
    hyperfine_cmd="hyperfine -r 4 --export-json "
    if [[ "$bench" =~ .*input ]]; then
        without_input="${bench%.input}"
        for input in ${benchsdir}/${without_input}*inputc ; do
            echo "$input"
            input_base=$(basename "$input")
            $hyperfine_cmd "${jsonprefix}_WITH_INPUT_${input_base}.json" "$bin < $input"
        done
    else
        $hyperfine_cmd "${jsonprefix}_NO_INPUT.json" "$bin"
    fi
done
