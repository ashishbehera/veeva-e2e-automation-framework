function extractProducts(cards, titleSel, priceSel, badgeSel) {
    if (!cards || cards.length === 0) return [];
    var result = [];
    cards.forEach(function(card) {
        var titleText = 'N/A', priceText = 'N/A', badgeText = 'N/A';
        try { var t = card.querySelector(titleSel); if(t?.innerText) titleText = t.innerText.trim(); } catch(e) {}
        try { var p = card.querySelector(priceSel); if(p?.innerText) priceText = p.innerText.trim(); } catch(e) {}
        try { var b = card.querySelector(badgeSel); if(b?.innerText) badgeText = b.innerText.trim(); } catch(e) {}
        result.push({Title: titleText, Price: priceText, TopSeller: badgeText});
    });
    return result;
}
