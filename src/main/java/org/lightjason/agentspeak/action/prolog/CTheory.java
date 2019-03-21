/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.prolog;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * creates theory objects by string input.
 * The action creates for each argument an item within the theory
 * and returns the theory input
 *
 * {@code T = .prolog/theory( "dosomethin(X) :- X is 5" );}
 */
public final class CTheory extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5362284489249927608L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CTheory.class, "prolog" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        p_return.add(
            CRawTerm.of(
                theory(
                    CCommon.flatten( p_argument )
                           .filter( i -> Objects.nonNull( i.raw() ) )
                           .map( ITerm::<String>raw )
                           .collect( Collectors.joining( "" ) ),
                    p_context
                )
            )
        );

        return Stream.of();
    }

    /**
     * create theory object and catch exception
     *
     * @param p_theory theory structure
     * @param p_context agent context
     * @return theory object
     */
    private static Theory theory( @Nonnull final String p_theory, @Nonnull final IContext p_context )
    {
        try
        {
            return new Theory( p_theory );
        }
        catch ( final InvalidTheoryException l_exception )
        {
            throw new CExecutionException( p_context, l_exception );
        }
    }

}
