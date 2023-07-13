#ifndef MMAPUTIL_H
#define MMAPUTIL_H
#include <map>
#include <iostream>
#include <vector>
template <typename KEY, typename VALUE, typename COMPARE = std::less<KEY>>
class multimap_util
{
private:
    std::multimap<KEY, VALUE, COMPARE> *source;

public:
    multimap_util(std::multimap<KEY, VALUE, COMPARE> &source)
    {
        this->source = &source;
    }

    // a replace_key-jel egy kulcsot lehet átcserélni egy másikra,
    multimap_util<KEY, VALUE, COMPARE> &replace_key(KEY fromKey, KEY toKey)
    {
        if (0 == this->source->count(fromKey))
        {
            return *this;
        }
        typename std::multimap<KEY, VALUE, COMPARE>::iterator i;
        std::vector<VALUE> valuesWithNewKey;
        std::vector<typename std::multimap<KEY, VALUE, COMPARE>::iterator> elementsToBeErased;
        for (i = this->source->begin(); i != this->source->end(); i++)
        {
            if (i->first == fromKey)
            {
                VALUE value = i->second;
                valuesWithNewKey.push_back(value);
                elementsToBeErased.push_back(i);
            }
        }
        for (typename std::multimap<KEY, VALUE, COMPARE>::iterator i : elementsToBeErased)
        {
            this->source->erase(i);
        }
        for (VALUE value : valuesWithNewKey)
        {
            this->source->insert(std::make_pair(toKey, value));
        }
        return *this;
    }

    // swap-pel két kulcshoz tartozó értékeket lehet felcserélni,
    multimap_util<KEY, VALUE, COMPARE> &swap(KEY firstKey, KEY secondKey)
    {
        if (0 == this->source->count(firstKey) || 0 == this->source->count(secondKey))
        {
            return *this;
        }

        // Documentation:
        // https://en.cppreference.com/w/cpp/container/multimap/find
        // Finds an element with key equivalent to key.
        // If there are several elements with key in the container, any of them may be returned.

        // for example: multimap's content (key:value): (a:2), (a:3), (b:4)
        // swap(a,b) => (a:4), (a:4), (b:?)
        // 2||3 ?

        VALUE firstValue = this->source->find(firstKey)->second;
        VALUE secondValue = this->source->find(secondKey)->second;

        typename std::multimap<KEY, VALUE, COMPARE>::iterator i;
        for (i = this->source->begin(); i != this->source->end(); i++)
        {
            if (i->first == firstKey)
            {
                i->second = secondValue;
            }
            else if (i->first == secondKey)
            {
                i->second = firstValue;
            }
        }

        return *this;
    }

    // replace_value-val a multimapben lévő értékeket lehet átírni,
    multimap_util<KEY, VALUE, COMPARE> &replace_value(VALUE fromValue, VALUE toValue)
    {
        typename std::multimap<KEY, VALUE, COMPARE>::iterator i;
        for (i = this->source->begin(); i != this->source->end(); i++)
        {
            if (i->second == fromValue)
            {
                i->second = toValue;
            }
        }

        return *this;
    }
    
    // erase-zel a multimapből lehet törölni érték szerint,
    multimap_util<KEY, VALUE, COMPARE> &erase(VALUE valueToBeErased)
    {
        typename std::multimap<KEY, VALUE, COMPARE>::iterator i;
        std::vector<typename std::multimap<KEY, VALUE, COMPARE>::iterator> elementsToBeErased;
        for (i = this->source->begin(); i != this->source->end(); i++)
        {
            if (i->second == valueToBeErased)
            {
                elementsToBeErased.push_back(i);
            }
        }
        for (typename std::multimap<KEY, VALUE, COMPARE>::iterator i : elementsToBeErased)
        {
            this->source->erase(i);
        }

        return *this;
    }
};

#endif