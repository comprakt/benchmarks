#!/bin/bash
cd $(dirname "${BASH_SOURCE[0]}")

out="$1".inlined.java
cat "$1" > "$out"


for i in ../mjtest/tests/exec/lib/*.java; do
    if echo "$i" | grep -P "(Http|TerminalInput)"; then
        continue
    fi
    echo "" >> $out
    echo "/* BEGIN $i */" >> $out
    cat $i >> $out
    echo "" >> $out
    echo "/* END $i */" >> $out
done

sed -i -E 's/^package.*$//' "$out"
sed -i -E 's/^import.*$//' "$out"
sed -i -E 's/public class/class/' "$out"

